import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class PreRun {
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
        job.setJobName("prerun");
        job.setJarByClass(PreRun.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setReducerClass(Reduce.class);
        job.setNumReduceTasks(5);

        MultipleInputs.addInputPath(job, new Path(args[0]), SequenceFileInputFormat.class, Map.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), SequenceFileInputFormat.class, Map.class);
        MultipleInputs.addInputPath(job, new Path(args[2]), SequenceFileInputFormat.class, Map.class);

        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(args[3]));
        job.waitForCompletion(true);
    }
}


