/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.datalayer.resource.manager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ResourceManagerApplication {
//	@Autowired
//	ProviderLocalService service;
//
//	@Autowired
//	ResourceLocalService resourceService;

	public static void main(String[] args) {
		SpringApplication.run(ResourceManagerApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

//			System.out.println("Ready.");
			printBanner();

//			service.getProviders().forEach((key, p) -> System.out.println((key + ":" + p.getId())));
//
//			// test
//			Resource res = resourceService.create("default", "mat", SystemKeys.TYPE_SQL, "nullProvider", null);
//			System.out.println("resource " + res.getId() + " uri " + res.getUri());
//
//			// parse
//			String username = SqlUtil.getUsername(res.getUri());
//			System.out.println("resource " + res.getId() + " user " + SqlUtil.getUsername(res.getUri()));
//			System.out.println("resource " + res.getId() + " password " + SqlUtil.getPassword(res.getUri()));
//			System.out.println("resource " + res.getId() + " provider " + SqlUtil.getProvider(res.getUri()));
//			System.out.println("resource " + res.getId() + " endpoint " + SqlUtil.getEndpoint(res.getUri()));
//			System.out.println("resource " + res.getId() + " host " + SqlUtil.getHost(res.getUri()));
//			System.out.println("resource " + res.getId() + " port " + SqlUtil.getPort(res.getUri()));
//			System.out.println("resource " + res.getId() + " database " + SqlUtil.getDatabase(res.getUri()));

		};
	}

	public void printBanner() {
		System.out.println("======================================");
		System.out.println(" ____                _                ");
		System.out.println("|  _ \\ ___  __ _  __| |_   _          ");
		System.out.println("| |_) / _ \\/ _` |/ _` | | | |         ");
		System.out.println("|  _ <  __/ (_| | (_| | |_| |_        ");
		System.out.println("|_| \\_\\___|\\__,_|\\__,_|\\__, (_)       ");
		System.out.println(" :resourcemanager      |___/          ");
		System.out.println("======================================");
	}

}
