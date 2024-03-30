package com.cse5382.assignment.Exceptions;

public class PhoneBookEmptyException extends RuntimeException {

   public PhoneBookEmptyException() {
      super("Phonebook is empty");
   }
    
}
