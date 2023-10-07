package com.example.storeservice.mappers;

import com.example.storeservice.DTOs.StoreDTO;
import com.example.storeservice.entities.Store;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    Store ToStore(StoreDTO storeDTO);

    StoreDTO ToStoreDTO(Store store);
}
