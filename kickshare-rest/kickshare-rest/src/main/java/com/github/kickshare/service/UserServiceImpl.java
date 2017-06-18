package com.github.kickshare.service;

import com.github.kickshare.db.dao.BackerRepository;
import com.github.kickshare.db.jooq.tables.daos.AddressDao;
import com.github.kickshare.db.jooq.tables.daos.BackerDao;
import com.github.kickshare.db.jooq.tables.daos.CityDao;
import com.github.kickshare.db.jooq.tables.pojos.Backer;
import com.github.kickshare.db.jooq.tables.pojos.City;
import com.github.kickshare.mapper.ExtendedMapper;
import com.github.kickshare.security.BackerDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * @author Jan.Kucera
 * @since 26.4.2017
 */
@Service
@AllArgsConstructor
public class UserServiceImpl {
    //@TODO make interface
    private ExtendedMapper dozer;
    private BackerRepository backerRepository;
    private UserDetailsManager userManager;
    private BackerDao backerDao;
    private PasswordEncoder encoder;
    private AddressDao addressDao;
    private CityDao cityDao;

    public UserDetails createUser(String email, Integer cityId) {
//        Backer backer = new Backer(null, userInfo.getEmail(), userInfo.getName(), userInfo.getSurname());
//        Long personId = backerRepository.createReturningKey(backer);
//        Address address = userInfo.getAddress();
//        com.github.kickshare.db.jooq.tables.pojos.Address dbAddress = new com.github.kickshare.db.jooq.tables.pojos.Address(null, personId, address.getStreet(),
//                address.getCity(), address.getPostalCode());
//        addressDao.insert(dbAddress);
        City city = cityDao.fetchOneById(cityId);
        Long id = backerRepository.createReturningKey(new Backer(null, email, "Testing", "Backer", new Float(5.0), new Float(5.0)));
        BackerDetails userToStore = new BackerDetails(email, encoder.encode("user"), id);
        userManager.createUser(userToStore);
        //@TODO insert address
        return userManager.loadUserByUsername(email);
    }

}
