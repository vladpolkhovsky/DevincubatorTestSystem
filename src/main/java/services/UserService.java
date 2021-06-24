package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import providers.AppContextProvider;
import repository.dao.entities.User;
import repository.managers.eager.EagerManager;
import repository.managers.lazy.LazyManager;

@Service
public class UserService {

    private UserServiceFacade lazyInstance;

    private UserServiceFacade eagerInstance;

    @Autowired
    public void setUserLazyManager(LazyManager<User> userLazyManager) {
        lazyInstance = new UserServiceFacade(userLazyManager);
    }

    @Autowired
    public void setUserEagerManager(EagerManager<User> userEagerManager) {
        eagerInstance = new UserServiceFacade(userEagerManager);
    }

    public UserServiceFacade getEagerInstance() {
        return eagerInstance;
    }

    public UserServiceFacade getLazyInstance() {
        return lazyInstance;
    }

    public static class UserServiceFacade {

        private final LazyManager<User> manager;

        private UserServiceFacade(LazyManager<User> manager) {
            this.manager = manager;
        }

        public User getUserById(int id) {
            return manager.read(id);
        }

        public User getUserByLogin(String login) throws UsernameNotFoundException {
            return manager.executeSql("FROM User where login = '" + login + "'")
                    .stream()
                    .filter(user -> user.getLogin().equals(login))
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("No user with name '" + login + "'"));
        };

    }

    public boolean isLoginRegistered(String login) {
        try {
            getLazyInstance().getUserByLogin(login);
            return true;
        } catch (UsernameNotFoundException ex) {
            return false;
        }
    }

    public void registerNewUser(String login, String firstName, String lastName, String password) {
        User user = AppContextProvider.getAppContext().getBean(User.class);

        user.setLogin(login);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setRole("ROLE_USER");

        System.out.println(user);

        getLazyInstance().manager.create(user);
    }

    public UserSecurityService.AuthorizedUser getUserFromSession() {
        UserDetails principal = (UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        if (principal instanceof  UserSecurityService.AuthorizedUser) {
            return (UserSecurityService.AuthorizedUser)principal;
        }

        return new UserSecurityService.AuthorizedUser(
                getLazyInstance().getUserByLogin(principal.getUsername()),
                principal.getAuthorities()
        );
    }

}
