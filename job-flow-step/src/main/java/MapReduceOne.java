import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class MapReduceOne {
    /**
     * inputs:
     * arg[0] - 1-gram
     * arg[1] - 2-gram
     * arg[2] - 3-gram
     * outputs:
     * arg[3]
     */
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJobName("mapreduceone");
        job.setJarByClass(MapReduceOne.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setReducerClass(ReduceF.class);
        job.setNumReduceTasks(5);
        for (int i = 0; i < args.length - 1; i++)
            MultipleInputs.addInputPath(job, new Path(args[i]), TextInputFormat.class, MapF.class);

        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(args[args.length - 1]));
        job.waitForCompletion(true);
    }
}


