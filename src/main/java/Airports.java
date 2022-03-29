import java.io.*;
import java.util.*;

public class Airports {
    
    private static final String PATH_TO_PROPERTIES = "src/main/resources/config.properties";
    private static int count;

    public static void main(String[] args) {
        count = 0;
        try {
            int numberColumn=getColumn(args);

            File file = new File(args[0]);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            System.out.print("Введите строку: ");
            Scanner sc = new Scanner(System.in);
            String dataInput = sc.nextLine();

            long beforeTime = System.currentTimeMillis();

            Map<String, List<String>> result=filterFile(reader,numberColumn,dataInput);

            long afterTime = System.currentTimeMillis();
            long time = afterTime - beforeTime;
            for(Map.Entry<String,List<String>> entry:result.entrySet()){
            System.out.println(String.format("%s:",entry.getKey()));
            for(String line: entry.getValue()){
                System.out.println(String.format("\t%s", line));
            }
        }
        System.out.println(String.format("Количество найденных строк: %d. Время, затраченное на поиск: %d.", count, time));
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
        } catch (NumberFormatException e){
            System.out.println("Номер колонки должен быть числовым");
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public static int getColumn(String[] args) throws IOException{
        int numberColumn=0;
        if(args.length>1){
            numberColumn=Integer.valueOf(args[1]);
        }else{
            Properties properties = new Properties();
            properties.load(new FileInputStream(PATH_TO_PROPERTIES));
            numberColumn = Integer.valueOf(properties.getProperty("column_number"));
        }
        if(numberColumn>14){
            throw new IllegalArgumentException("Номера колонки больше 14 не существует");
        }
        return numberColumn;
    }

    public static Map<String,List<String>> filterFile(BufferedReader reader,int numberColumn, String dataInput) throws IOException {
        String[] listLane = null;
        String column = null;
        String line = reader.readLine();
        Map<String, List<String>> result = new TreeMap<>();
        while (line != null) {
            listLane = line.split("\"?,\"?");
            column = listLane[numberColumn - 1];
            if (column.startsWith(dataInput)) {
                if(result.containsKey(column)){
                    result.get(column).add(line);
                }else{
                    List<String> list = new ArrayList<>();
                    list.add(line);
                    result.put(column,list);
                }
                count++;
            }
            line = reader.readLine();
        }
        return result;
    }
}
