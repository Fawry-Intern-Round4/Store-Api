package com.example.storeservice.mappers;

import com.example.storeservice.dtos.StoreDTO;
import com.example.storeservice.entities.Store;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    Store toStore(StoreDTO storeDTO);

    StoreDTO toStoreDTO(Store store);
}
