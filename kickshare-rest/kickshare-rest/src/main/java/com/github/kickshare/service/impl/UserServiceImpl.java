package com.github.kickshare.service.impl;

import com.github.kickshare.db.dao.BackerRepository;
import com.github.kickshare.db.dao.UserRepositoryImpl;
import com.github.kickshare.db.jooq.tables.daos.AddressDao;
import com.github.kickshare.db.jooq.tables.daos.CityDao;
import com.github.kickshare.db.jooq.tables.pojos.City;
import com.github.kickshare.db.jooq.tables.pojos.Users;
import com.github.kickshare.domain.Address;
import com.github.kickshare.domain.Backer;
import com.github.kickshare.mapper.AddressMapper;
import com.github.kickshare.mapper.BackerMapper;
import com.github.kickshare.security.BackerDetails;
import com.github.kickshare.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private BackerRepository backerRepository;
    private UserDetailsManager userManager;
    private PasswordEncoder encoder;
    private AddressDao addressDao;
    private CityDao cityDao;
    private UserRepositoryImpl userRepository;

    @Override
    public UserDetails createUser(String email, Integer cityId) {
        Long id = backerRepository.createReturningKey(BackerMapper.MAPPER.toDB(new Backer(null, email, "Testing", "Backer", new Float(5.0), new Float(5.0))));
        BackerDetails userToStore = new BackerDetails(email, encoder.encode("user"), id, false);
        userManager.createUser(userToStore);

        final City city = cityDao.fetchOneById(cityId);
        Address address = new Address(null, id, null, null, city.getId(), null);
        addressDao.insert(AddressMapper.MAPPER.toDB(address));

        //@TODO insert address
        return userManager.loadUserByUsername(email);
    }

    @Override
    public UserDetails createUser(com.github.kickshare.domain.Backer backer, String password, Address address) {
        Long id = backerRepository.createReturningKey(BackerMapper.MAPPER.toDB(backer));

        BackerDetails userToStore = new BackerDetails(backer.getEmail(), encoder.encode(password), id, false);
        userManager.createUser(userToStore);

        address.setBackerId(id);

        addressDao.insert(AddressMapper.MAPPER.toDB(address));

        return userManager.loadUserByUsername(backer.getEmail());
    }

    @Override
    public boolean verifyUser(String token) {
        final Users user = userRepository.getUserByToken(token);
        if (user == null) {
            return false;
        }
        if (user.getEnabled()) {
            LOGGER.warn("User {} is already verified, skipping", user.getId());
        } else {
            user.setEnabled(true);
            userRepository.update(user);
        }
        return true;
    }
}
