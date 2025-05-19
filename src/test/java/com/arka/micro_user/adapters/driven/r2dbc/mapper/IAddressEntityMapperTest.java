package com.arka.micro_user.adapters.driven.r2dbc.mapper;

import com.arka.micro_user.adapters.driven.r2dbc.entity.AddressEntity;
import com.arka.micro_user.domain.model.AddressModel;
import com.arka.micro_user.data.AddressData;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class IAddressEntityMapperTest {

    private final IAddressEntityMapper mapper = Mappers.getMapper(IAddressEntityMapper.class);

    @Test
    void toModel_ShouldMapEntityToModel() {
        AddressEntity entity = AddressData.getClientAddressEntity();

        AddressModel model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals(entity.getStreet(), model.getStreet());
        assertEquals(entity.getCity(), model.getCity());
        assertEquals(entity.getCountry(), model.getCountry());
        assertEquals(entity.getNomenclature(), model.getNomenclature());
        assertEquals(entity.getState(), model.getState());
        assertEquals(entity.getZipCode(), model.getZipCode());
    }

    @Test
    void toEntity_ShouldMapModelToEntity() {
        AddressModel model = AddressData.getClientAddress();

        AddressEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(model.getStreet(), entity.getStreet());
        assertEquals(model.getCity(), entity.getCity());
        assertEquals(model.getCountry(), entity.getCountry());
        assertEquals(model.getNomenclature(), entity.getNomenclature());
        assertEquals(model.getState(), entity.getState());
        assertEquals(model.getZipCode(), entity.getZipCode());
    }
}
