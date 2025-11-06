package com.guille.media.reproductor.powercine.mapper;

import com.guille.media.reproductor.powercine.dto.response.AccountDto;
import com.guille.media.reproductor.powercine.models.AccountJpaEntity;
import com.guille.media.reproductor.powercine.models.SecurityAccount;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto toAccountDto(AccountJpaEntity entity);
    SecurityAccount toSecurityAccount(AccountJpaEntity entity);
}
