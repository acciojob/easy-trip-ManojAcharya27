package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Repository
public class AirportRepository {


    HashMap<String,Airport> airportsDb=new HashMap<>();
    HashMap<Integer,Flight> flightsDb=new HashMap<>();

    List<Passenger> passengersDb=new ArrayList<>();

    public HashMap<Integer,List<Integer>> flightToPassengerDb = new HashMap<>();
    public  String addAirport(Airport airport){
        airportsDb.put(airport.getAirportName(),airport);
        return "SUCCESS";
    }



    public String getLargestAirportName(){
        int ans=0;
        Airport airport1=null;
        for (Airport airport: airportsDb.values()){
            if(airport.getNoOfTerminals()>ans){
                ans=airport.getNoOfTerminals();
                airport1=airport;
            }else if(ans==airport.getNoOfTerminals()){
                if(airport.getAirportName().compareTo(airport1.getAirportName())<0){
                    airport1=airport;
                }
            }
        }
        return airport1.getAirportName();
    }


    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity){
        double ans=Integer.MAX_VALUE;
        for(Flight flight: flightsDb.values()){
            if(flight.getFromCity().equals(fromCity)&&flight.getToCity().equals(toCity)){
                if(flight.getDuration()<ans)  ans=flight.getDuration();
            }
        }
        if(ans==Integer.MAX_VALUE) return -1;
        return  ans;
    }
    public int getNumberOfPeopleOn(Date date,String airportName){
         Airport airport=null;
         airport=airportsDb.get(airportName);
         if(Objects.isNull(airport)){
             return 0;
         }
         City city=airport.getCity();
         int count=0;
         for(Flight flight :flightsDb.values()){
             if(date.equals(flight.getFlightDate())){
                 if(flight.getToCity().equals(city)||flight.getToCity().equals(city)){
                     int flightId=flight.getFlightId();
                     count+=flightToPassengerDb.get(flightId).size();
                 }
             }
         }
         return  count;
    }


    public int calculateFlightFare(Integer flightId){
        int noOfPeople=flightToPassengerDb.get(flightId).size();
        return 3000+50*noOfPeople;

    }
   public void addFlight(Flight flight){
        flightsDb.put(flight.getFlightId(),flight);
   }

    public  String bookATicket(Integer flightId,Integer passengerId){
        if(Objects.isNull(flightToPassengerDb.get(flightId))&&(flightToPassengerDb.get(flightId)).size()<flightsDb.get(flightId).getMaxCapacity()){
            List<Integer> passengers =  flightToPassengerDb.get(flightId);

            if(passengers.contains(passengerId)){
                return "FAILURE";
            }

            passengers.add(passengerId);
            flightToPassengerDb.put(flightId,passengers);
            return "SUCCESS";
        }
        else if(Objects.isNull(flightToPassengerDb.get(flightId))){
            flightToPassengerDb.put(flightId,new ArrayList<>());
            List<Integer> passengers =  flightToPassengerDb.get(flightId);

            if(passengers.contains(passengerId)){
                return "FAILURE";
            }

            passengers.add(passengerId);
            flightToPassengerDb.put(flightId,passengers);
            return "SUCCESS";

        }
        return  "FAILURE";
        }


    public  String cancelATicket(Integer flightId,Integer passengerId){
        Flight flight=null;
        for(Flight flight1: flightsDb.values()){
            if(flight1.getFlightId()==flightId){
                flight=flight1;
            }
        }
        if(flight==null) return "FAILURE";
        List<Integer> passenger=flightToPassengerDb.get(flightId);
        if(passenger==null) return "FAILURE";
        else {
            if(passenger.contains(passengerId)){
                passenger.remove(passengerId);
            }
            return "SUCCESS";

        }
    }

   public int  countOfBookingsDoneByPassengerAllCombined(@PathVariable("passengerId")Integer passengerId){
        int count=0;
        for(Map.Entry<Integer,List<Integer>> entry: flightToPassengerDb.entrySet()){
            List<Integer> passengers=entry.getValue();
           for(Integer passenger: passengers){
               if(passenger==passengerId){
                   count++;
               }
           }
        }
        return count;
   }

    public  String getAirportNameFromFlightId(Integer flightId){
        Flight flight=null;
        for(Flight flight1: flightsDb.values()){
            if(flight1.getFlightId()==flightId){
                flight=flight1;
            }
        }
        if(flight!=null){
            City city=flight.getFromCity();
            for(Airport airport: airportsDb.values()){
                if(airport.getCity().compareTo(city)==0){
                    return airport.getAirportName();
                }
            }
        }
        return null;
    }

   public int calculateRevenueOfAFlight(Integer flightId){
        int noOfPeople=flightToPassengerDb.get(flightId).size();
        int flare=(noOfPeople*(noOfPeople-1))*25;
        int fixedFlare=3000*noOfPeople;
        return  fixedFlare+flare;
   }

    public  void addPassenger(Passenger passenger){
        passengersDb.add(passenger);
    }



}


