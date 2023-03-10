package co.edu.umanizales.manage_store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor

@Getter
@Setter
public class Store {
    private String code;
    private String name;

    public Store() {

    }
}
