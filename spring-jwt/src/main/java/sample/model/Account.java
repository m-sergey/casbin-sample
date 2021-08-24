package sample.model;

import lombok.Data;

@Data
public class Account {

    /** Unique account identifier */
    Long id;

    /** Account number */
    String num;

    /** Client's identifier */
    String client;

    /** Client's manager */
    String manager;
}
