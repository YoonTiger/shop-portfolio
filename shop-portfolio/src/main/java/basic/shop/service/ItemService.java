package basic.shop.service;

import basic.shop.entity.Item;
import basic.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Long save(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    public Item findById(Long id) {
        return itemRepository.findById(id).orElseThrow(()->new NoSuchElementException("없는 상품입니다"));
    }

    public List<Item> findAll(){
        return itemRepository.findAll();
    }

    public void delete(Long id) {
        itemRepository.delete(id);
    }

    public Long update(Item item){
        itemRepository.update(item);
        return item.getId();
    }

    public void updateAll(List<Item> items){
        itemRepository.updateAll(items);
    }

}
