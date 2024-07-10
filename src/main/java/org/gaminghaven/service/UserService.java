package org.gaminghaven.service;

import org.gaminghaven.entities.User;
import org.gaminghaven.exceptions.InvalidLoginCreds;
import org.gaminghaven.exceptions.ProductNotFound;
import org.gaminghaven.exceptions.PersistenceException;
import org.gaminghaven.exceptions.UserNotFound;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * create new user
     *
     * @param user
     * @return
     */
    Map<String, String> createNewUser(User user) throws UserNotFound, PersistenceException;

    /**
     * @param email
     * @param password
     * @return
     * @throws InvalidLoginCreds
     */
    Map<String, String> loginUser(String email, String password, HttpServletResponse response, HttpServletRequest request) throws InvalidLoginCreds;

    /**
     * @param email
     * @return
     */
    User getUserByEmail(String email) throws UserNotFound;

    /**
     * @return
     */
    List<User> getAllUsers();

    /**
     * @param email
     * @return
     * @throws UserNotFound
     */
    Map<String, ?> getDashBoardInfo(String email) throws UserNotFound;

    void addSavedListing(int listingId) throws UserNotFound, ProductNotFound;

    void removeSavedListing(int listingId) throws UserNotFound, ProductNotFound;
}
