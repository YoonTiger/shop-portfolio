package basic.shop.repository;

import basic.shop.entity.Item;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemRepository {

    void save(Item item);

    Optional<Item> findById(Long id);

    List<Item> findByIds(List<Long> itemIds);

    List<Item> findAll();



    void delete(Long id);

    void update(Item item);

    void updateAll(List<Item> item);

}
