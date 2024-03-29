package com.cse5382.assignment.Model;

import java.util.Objects;



import javax.persistence.*;



@Entity
@Table(name="phonebook")
public class PhoneBookEntry   {

  @Column
  private String name = null;


  @Column
  @Id
  private String phoneNumber = null;

  public PhoneBookEntry name(String name) {
    this.name = name;
    return this;
  }


    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PhoneBookEntry phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }


    public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PhoneBookEntry phoneBookEntry = (PhoneBookEntry) o;
    return Objects.equals(this.name, phoneBookEntry.name) &&
        Objects.equals(this.phoneNumber, phoneBookEntry.phoneNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, phoneNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PhoneBookEntry {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}
