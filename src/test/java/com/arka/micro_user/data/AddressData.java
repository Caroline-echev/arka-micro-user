package com.arka.micro_user.data;

import com.arka.micro_user.adapters.driven.r2dbc.entity.AddressEntity;
import com.arka.micro_user.adapters.driving.reactive.dto.request.AddressRequest;
import com.arka.micro_user.domain.model.AddressModel;

import javax.swing.plaf.PanelUI;

public class AddressData {
    public static AddressModel getClientAddress() {
        AddressModel address = new AddressModel();
        address.setStreet("Calle 1");
        address.setCity("Bogota");
        address.setCountry("Colombia");
        address.setNomenclature("Calle");
        address.setState("Bogota");
        address.setZipCode("11001");
        return address;
    }
    public static AddressRequest getClientAddressRequest() {
        AddressRequest address = new AddressRequest();
        address.setStreet("Calle 1");
        address.setCity("Bogota");
        address.setCountry("Colombia");
        address.setNomenclature("Calle");
        address.setState("Bogota");
        address.setZipCode("11001");
        return address;
    }
    public static AddressEntity getClientAddressEntity() {
        AddressEntity address = new AddressEntity();
        address.setStreet("Calle 1");
        address.setCity("Bogota");
        address.setCountry("Colombia");
        address.setNomenclature("Calle");
        address.setState("Bogota");
        address.setZipCode("11001");
        return address;
    }
}
