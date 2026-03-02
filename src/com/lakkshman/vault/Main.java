package com.lakkshman.vault;

import com.lakkshman.vault.service.VaultService;

public class Main {

    public static void main(String[] args) throws Exception {
        VaultService service = new VaultService();
        service.run();
    }
}