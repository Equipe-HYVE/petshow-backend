package hyve.petshow.mock;

import hyve.petshow.domain.embeddables.Login;

public class LoginMock {
    public static Login login(){
        var login = new Login();

        login.setEmail("teste@teste.com");
        login.setSenha("blabla");

        return login;
    }
}
