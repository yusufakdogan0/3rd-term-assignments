import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException,IOException{

        if (args.length != 2) {
            System.out.println("Usage: java Main <input_file> <output_file>");
            System.exit(1);
        }

        String inputFileName = args[0];
        String outputFileName = args[1];
        //String inputFileName = "C:\\Users\\DELL\\Desktop\\Java\\CMPE250\\First assignment\\src\\large2.txt";
        //String outputFileName = "large2out.txt";
        try {
            // Open input and output files
            BufferedReader input = new BufferedReader(new FileReader(inputFileName));
            BufferedWriter output = new BufferedWriter(new FileWriter(outputFileName));
            // taking initial boss
            String bossLine = input.readLine();
            Member.boss.name = bossLine.split(" ",2)[0];
            Member.boss.gms = Double.parseDouble(bossLine.split(" ",2)[1]);
            // taking rest of the input
            String line;
            while ((line = input.readLine()) != null) {
                // split the line so that line[0] is the operation that we are wanted
                String[] lineArray = line.split(" ");
                String operation = lineArray[0];
                // pass the empty lines
                if (operation.equals("")){continue;}
                // divide operation
                if (operation.equals("INTEL_DIVIDE")){
                    //I have defined a static variable for member class which will initially be 0 for each division operation
                    //divide operation will update it and update method will be explained there
                    Member.boss.divide();
                    output.write("Division Analysis Result: " + Member.divisonNumber);
                    output.newLine();
                    Member.divisonNumber=0;
                    continue;
                }
                // if it is not a divide operation we will need a name and gms to continue
                String name = lineArray[1];
                double gms = Double.parseDouble(lineArray[2]);
                if (operation.equals("MEMBER_IN")){
                    // join will update output file and add the member. Therefore no need for additional step
                    Member.boss.join(new Member(name,gms),output);
                    continue;
                }
                if (operation.equals("MEMBER_OUT")){
                    //find function returns a member for a given gms
                    //leave function removes a given member from family and returns the text that will be written on output file
                    String str = Member.boss.find(gms).leave();
                    output.write(str);
                    output.newLine();
                    continue;
                }
                if (operation.equals("INTEL_RANK")){
                    //result is the arraylist that will include members
                    //findrank function finds rank of the member that has a given gms
                    //findMembersInRank function finds all the members occupies a particular rank and returns the arraylist corresponding them
                    ArrayList<Member> result = Member.boss.findMembersInRank(Member.boss.findRank(gms));
                    //update the output for the arraylist
                    output.write("Rank Analysis Result: ");
                    for (int counter=0; counter < result.size(); counter++){
                        Member mem = result.get(counter);
                        output.write(mem.name + " " + String.format("%,.3f",mem.gms));
                        if (counter == result.size()-1){
                            output.newLine();
                        }
                        else {output.write(" ");}
                    }
                    continue;
                }
                if (operation.equals("INTEL_TARGET")){
                    //we will need a second person for targeting
                    double gms2 = Double.parseDouble(lineArray[4]);
                    //target funcion takes 2 gms as input and finds their closest superior and if one of them is boss it returns boss.
                    Member target = Member.boss.target(gms,gms2);
                    //update the output
                    output.write( "Target Analysis Result: " + target.name + " " + String.format("%,.3f",target.gms));
                    output.newLine();
                }
            }
            //Let's not forget closing the files
            input.close();
            output.close();
            //I don't know what this function is doing. I have copied the code necessary for updating files from chatgpt
            //tho since I assume we won't get the exception it is unimportant
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}