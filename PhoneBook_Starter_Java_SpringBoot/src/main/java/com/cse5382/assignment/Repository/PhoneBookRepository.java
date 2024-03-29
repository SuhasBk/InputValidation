package com.cse5382.assignment.Repository;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.IntStream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cse5382.assignment.Model.PhoneBookEntry;

@Repository
public interface PhoneBookRepository extends JpaRepository<PhoneBookEntry, String> {

    void deleteByPhoneNumber(String phoneNumber);

    // List<PhoneBookEntry> list = new ArrayList<>();
    // public List<PhoneBookEntry> list(){
    //     return  list;    
    // }

    // public void save(String name, String phoneNumber){
    //     PhoneBookEntry phoneBookEntry = new PhoneBookEntry();
    //     phoneBookEntry.setPhoneNumber(phoneNumber);
    //     phoneBookEntry.setName(name);
    //     list.add(phoneBookEntry);
    // }


    // public void deleteByName(String name){
    //     int index = IntStream.range(0, list.size())
    //             .filter(i -> list.get(i).getName().equals(name))
    //             .findFirst()
    //             .orElse(-1);
    //     if(index!=-1){
    //         list.remove(index);
    //     }
    // }


    // public void deleteByNumber(String phoneNumber){
    //     int index = IntStream.range(0, list.size())
    //             .filter(i -> list.get(i).getPhoneNumber().equals(phoneNumber))
    //             .findFirst()
    //             .orElse(-1);
    //     if(index!=-1){
    //         list.remove(index);
    //     }
    // }

}
