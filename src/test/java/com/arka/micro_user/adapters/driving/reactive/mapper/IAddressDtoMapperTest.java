package com.arka.micro_user.adapters.driving.reactive.mapper;

import com.arka.micro_user.adapters.driving.reactive.dto.request.AddressRequest;
import com.arka.micro_user.data.AddressData;
import com.arka.micro_user.domain.model.AddressModel;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class IAddressDtoMapperTest {

    private final IAddressDtoMapper mapper = Mappers.getMapper(IAddressDtoMapper.class);

    @Test
    void testToModel() {
        AddressRequest request = AddressData.getClientAddressRequest();

        AddressModel model = mapper.toModel(request);

        assertNotNull(model);
        assertEquals(request.getStreet(), model.getStreet());
        assertEquals(request.getCity(), model.getCity());
        assertEquals(request.getCountry(), model.getCountry());
        assertEquals(request.getNomenclature(), model.getNomenclature());
        assertEquals(request.getState(), model.getState());
        assertEquals(request.getZipCode(), model.getZipCode());
    }
}
