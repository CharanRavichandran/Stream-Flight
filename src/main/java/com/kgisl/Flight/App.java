package com.kgisl.Flight;


import java.awt.RenderingHints.Key;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
       
        String fileName = "src/main/resource/Flight.csv";
        Path myPath = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

				
            HeaderColumnNameMappingStrategy<Flight> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Flight.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(Flight.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Flight> emp = csvToBean.parse();
Map<String, Long> departureCounting = emp.stream()
.collect(Collectors.groupingBy(Flight::getDeparture, Collectors.counting()));
System.out.println(departureCounting);
//
Predicate<Flight> depatrurePredicate = x -> "Coimbatore".equals(x.getDeparture());
Predicate<Flight> arrivalPredicate = x -> "London".equals(x.getArrival());
System.out.println(emp.stream().filter(depatrurePredicate.and(arrivalPredicate)).map(x -> x.getId())
        .collect(Collectors.toList()));
        
		// Airlines name wise grouping
		Map<String, Set<String>> result = emp.stream()
        .collect(Collectors.groupingBy(Flight::getName, Collectors.mapping(Flight::toString, Collectors.toSet())));
System.out.println(result);
System.out.println("Duplicate:"+emp.stream().count());
///Unique value using toString().
Set<Flight> setFlight = emp.stream()
.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Flight::toString))));
//
Set<Flight> let = emp.stream()
.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Flight::getId)
.thenComparing(Comparator.comparing(Flight::getArrival)
.thenComparing(Comparator.comparing(Flight::getDeparture)
.thenComparing(Comparator.comparing(Flight::getName)))))));
System.out.println("id:  "+let);

System.out.println("///////////////////////////////////");
List<Flight> round=new ArrayList<Flight>();
String a,d;
for(Flight f:emp){
    a=f.getArrival();
    d=f.getDeparture();
    for(Flight g:emp){
        if((a==g.getDeparture())&& (d==g.getArrival())){
            round.add(g);
            System.out.println(g.getDeparture()+" "+a+"\n"+g.getArrival()+" "+d);
        }
        
    }

}
System.out.println(round);
//System.out.println("Unique:"+setFlight.stream().count());
    }
}
}