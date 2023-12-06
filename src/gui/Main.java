package gui;

import environment.LocalBoard;
import remote.Client;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("enter Address of Server: ");
		String address = scanner.nextLine();
		System.out.println("enter PORT number: ");
		String port = scanner.nextLine();
		new Client(address, port).start();
	}
}
