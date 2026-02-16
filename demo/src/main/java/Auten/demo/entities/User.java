package Auten.demo.entities;

//entidade que representa um usuário do sistema, com atributos como nome, email e senha.

import jakarta.persistence.*;

import java.util.Set;

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    @Column (name =  " user_id")
    private String id;

    @Column (unique = true)
    private String username;
    private String password;

    @ManyToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

   private Set<Role> roleS;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public enum values{

        ADMIN(1L),

        BASIC (2L) ;

        final long roleId;

        values(long roleId) {
            this.roleId = roleId;
        }

    }

}
