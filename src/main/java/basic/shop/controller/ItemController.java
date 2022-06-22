package basic.shop.controller;

import basic.shop.SessionConst;
import basic.shop.controller.Dto.ItemDTO;
import basic.shop.controller.messageUtil.MessageUtil;
import basic.shop.entity.Item;
import basic.shop.entity.Member;
import basic.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/add")
    public String addItemForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                              Model model) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setSellerName(loginMember.getName());
        model.addAttribute("item", itemDTO);
        return "itemView/itemAdd";
    }

    @PostMapping("/add")
    public String addItem(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                          @Validated @ModelAttribute("item") ItemDTO itemDTO, BindingResult bindingResult){

        if(itemDTO.getPrice() != null && itemDTO.getStockQuantity() != null){
            int resultPrice = itemDTO.getPrice() * itemDTO.getStockQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice},null);
            }
        }
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "itemView/itemAdd";
        }

        Item item = Item.createItem(loginMember, itemDTO);
        itemService.save(item);
        return "redirect:/item/list";
    }

    @GetMapping("/list")
    public String listItem(Model model) {
        List<Item> all = itemService.findAll();
        List<ItemDTO> items = all.stream().map(ItemDTO::new).collect(Collectors.toList());
        model.addAttribute("items", items);
        return "itemView/itemList";
    }

    @GetMapping("/{id}/info")
    public String infoItem(@PathVariable Long id, Model model) {
        Item findItem = itemService.findById(id);
        model.addAttribute("item", new ItemDTO(findItem));
        return "itemView/itemInfo";
    }

    @GetMapping("/{id}/edit")
    public String editItemForm(@PathVariable Long id, Model model){

        Item findItem = itemService.findById(id);
        model.addAttribute("item", new ItemDTO(findItem));
        return "itemView/itemEdit";
    }

    @PostMapping("/{id}/edit")
    public String editItem(@Validated @ModelAttribute("item") ItemDTO itemDTO, BindingResult bindingResult,
                           @PathVariable Long id, HttpServletResponse response) throws IOException {

        if(itemDTO.getPrice() != null && itemDTO.getStockQuantity() != null){
            int resultPrice = itemDTO.getPrice() * itemDTO.getStockQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice},null);
            }
        }
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "itemView/itemEdit";
        }

        Item item = Item.updateItem(itemDTO);
        itemService.update(item);
        MessageUtil.alertAndMovePage(response, "수정이 완료되었습니다","/item/"+ id + "/info");
        return "itemView/itemInfo";
    }

    @GetMapping("/{id}/delete")
    public String deleteItem(@PathVariable Long id, HttpServletResponse response) throws IOException {

        itemService.delete(id);
        MessageUtil.alertAndMovePage(response, "삭제가 완료되었습니다","/item/list");
        return "itemView/itemList";
    }








}
