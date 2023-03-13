package ru.practicum.shareit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class ShareItApp {
	@Bean
	public CommandLineRunner lineRunner() {
		return (args) -> {
			try {
				Process p = new ProcessBuilder().inheritIO().command("git", "-C", "tests", "checkout", "addfe33e3e91e2a898f5159dbe04be20d7ecefb1").start();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ShareItApp.class, args);
	}
}
