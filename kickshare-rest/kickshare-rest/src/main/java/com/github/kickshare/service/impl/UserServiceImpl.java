package com.github.kickshare.service.impl;

import static com.github.kickshare.mapper.EntityMapper.address;
import static com.github.kickshare.mapper.EntityMapper.backer;

import com.github.kickshare.db.dao.BackerRepository;
import com.github.kickshare.db.dao.impl.UserRepositoryImpl;
import com.github.kickshare.db.jooq.tables.daos.AddressDaoDB;
import com.github.kickshare.db.jooq.tables.daos.CityDaoDB;
import com.github.kickshare.db.jooq.tables.pojos.CityDB;
import com.github.kickshare.db.jooq.tables.pojos.UsersDB;
import com.github.kickshare.domain.Address;
import com.github.kickshare.domain.Backer;
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
    private AddressDaoDB addressDao;
    private CityDaoDB cityDao;
    private UserRepositoryImpl userRepository;

    @Override
    public UserDetails createUser(String email, Integer cityId) {
        Long id = backerRepository.createReturningKey(backer().toDB(new Backer(null, email, "Testing", "Backer", new Float(5.0), new Float(5.0))));
        BackerDetails userToStore = new BackerDetails(email, encoder.encode("user"), id, false);
        userManager.createUser(userToStore);

        final CityDB city = cityDao.fetchOneByIdDB(cityId);
        Address address = new Address(null, id, null, null, city.getId(), null);
        addressDao.insert(address().toDB(address));

        //@TODO insert address
        return userManager.loadUserByUsername(email);
    }

    @Override
    public BackerDetails createUser(Backer backer, String password, Address address) {
        Long id = backerRepository.createReturningKey(backer().toDB(backer));

        BackerDetails userToStore = new BackerDetails(backer.getEmail(), encoder.encode(password), id, false);
        userManager.createUser(userToStore);

        address.setBackerId(id);

        addressDao.insert(address().toDB(address));
        BackerDetails userDetails = (BackerDetails) userManager.loadUserByUsername(backer.getEmail());
        return userDetails;
    }

    @Override
    public Backer getUserByEmail(final String email) {
        return backer().toDomain(backerRepository.findByEmail(email));
    }

    @Override
    public void changePassword(BackerDetails backer, String password) {
        BackerDetails userToStore = new BackerDetails(backer, encoder.encode(password));
        userManager.updateUser(userToStore);
    }

    @Override
    public boolean verifyUser(String token) {
        final UsersDB user = userRepository.getUserByToken(token);
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
