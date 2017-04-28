package com.github.kickshare.service;

import com.github.kickshare.db.dao.BackerRepositoryImpl;
import com.github.kickshare.db.h2.tables.daos.AddressDao;
import com.github.kickshare.domain.UserInfo;
import com.github.kickshare.mapper.ExtendedMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Jan.Kucera
 * @since 26.4.2017
 */
@Service
@AllArgsConstructor
public class UserServiceImpl {
    //@TODO make interface
    private BackerRepositoryImpl backerRepository;
    private AddressDao addressDao;
    private ExtendedMapper dozer;

    public void createUser(UserInfo userInfo) {
//        Backer backer = new Backer(null, userInfo.getEmail(), userInfo.getName(), userInfo.getSurname());
//        Long personId = backerRepository.createReturningKey(backer);
//        Address address = userInfo.getAddress();
//        com.github.kickshare.db.h2.tables.pojos.Address dbAddress = new com.github.kickshare.db.h2.tables.pojos.Address(null, personId, address.getStreet(),
//                address.getCity(), address.getPostalCode());
//        addressDao.insert(dbAddress);
    }
}
