import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * The entry point for the Sequence Writer App example,
 * which setup the Hadoop job with MapReduce Classes
 *
 * @author Raman
 *
 */
public class SequenceFileWriterApp extends Configured implements Tool
{
    /**
     * Main function which calls the run method and passes the args using ToolRunner
     * @param args Two arguments input and output file paths
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        int exitCode = ToolRunner.run(new SequenceFileWriterApp(), args);
        System.exit(exitCode);
    }

    /**
     * Run method which schedules the Hadoop Job
     * @param args Arguments passed in main function
     */
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.printf("Usage: %s needs two arguments   files\n",
                    getClass().getSimpleName());
            return -1;
        }

        //Initialize the Hadoop job and set the jar as well as the name of the Job
        Job job = new Job();
        job.setJarByClass(SequenceFileWriterApp.class);
        job.setJobName("SequenceFileWriter");

        //Add input and output file paths to job based on the arguments passed
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        //Set the MapClass and ReduceClass in the job
        job.setMapperClass(SequenceFileWriterMapper.class);

        //Setting the number of reducer tasks to 0 as we do not 
        //have any reduce tasks in this example. We are only concentrating on the Mapper
        job.setNumReduceTasks(0);


        //Wait for the job to complete and print if the job was successful or not
        int returnValue = job.waitForCompletion(true) ? 0:1;

        if(job.isSuccessful()) {
            System.out.println("Job was successful");
        } else if(!job.isSuccessful()) {
            System.out.println("Job was not successful");
        }

        return returnValue;
    }

    /**
     * Mapper class of the MapReduce package.
     * It just writes the input key-value pair to the context
     *
     * @author Raman
     *
     */
    public static class SequenceFileWriterMapper extends Mapper {

        /**
         * This is the map function, it does not perform much functionality.
         * It only writes key and value pair to the context
         * which will then be written into the sequence file.
         */
        protected void map(Text key, Text value,Context context) throws IOException, InterruptedException {
            context.write(key, value);
        }

    }
}