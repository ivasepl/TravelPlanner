package com.tp.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tp.jpa.UsersEntity;

import java.io.IOException;
import java.util.Base64;

public class UsersEntitySerializer extends JsonSerializer<UsersEntity> {

    @Override
    public void serialize(UsersEntity usersEntity, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", usersEntity.getUserId());
        jsonGenerator.writeStringField("username", usersEntity.getUsername());
        jsonGenerator.writeStringField("firstName", usersEntity.getFirstName());
        jsonGenerator.writeStringField("lastName", usersEntity.getLastName());
        jsonGenerator.writeStringField("email", usersEntity.getEmail());
        jsonGenerator.writeStringField("address", usersEntity.getAddress());
        if(usersEntity.getUserImage()!= null) {
            jsonGenerator.writeStringField("userImage", new String(Base64.getEncoder().encode(usersEntity.getUserImage())));
        }else{
            jsonGenerator.writeStringField("userImage", null);
        }
        jsonGenerator.writeBooleanField("active",usersEntity.isActive());
        jsonGenerator.writeEndObject();

    }
}
