import java.io.IOException;

import java.io.File;
import org.apache.commons.io.FileUtils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//acura 168
public class CarsTrucks {

    public static class TokenizerMapper
            extends Mapper<LongWritable, Text, TwoTexts, TwoIntegers>{

        private Text regionId = new Text(); //field 17
        private Text manufacturer = new Text(); //field 2
        private Text price = new Text();

        public void map(LongWritable offset, Text lineText, Context context
        ) throws IOException, InterruptedException {
            if (offset.get() != 0) {
                String line = lineText.toString();
                int i = 0;
                for (String word : line.split("\\^")){
                    if (i == 2) {
                        manufacturer.set(word);
                    }
                    if (i == 17) {
                        regionId.set(word);
                    }
                    if (i == 0) {
                        price.set(word);
                    }
                    TwoIntegers numbers = new TwoIntegers(1, 0);
                    TwoTexts mapKey = new TwoTexts();

                    mapKey.set(regionId, manufacturer);
                    numbers.set(1, Long.parseLong(price.toString()));

                    context.write(mapKey, numbers);
                    i++;
                }
            }

        }
    }

    public static class IntSumReducer
            extends Reducer<TwoTexts,TwoIntegers,TwoTexts,TwoIntegers> {
        private TwoIntegers result = new TwoIntegers(0, 0);

        public void reduce(TwoTexts key, Iterable<TwoIntegers> values,
                           Context context
        ) throws IOException, InterruptedException {
            long sumCar = 0;
            long sumPrice = 0;
            for (TwoIntegers val : values) {
                sumCar += val.getFirstValue();
                sumPrice += val.getSecondValue();
            }
            if (sumCar > 10) {
                result.set(sumCar, sumPrice);
                context.write(key, result);
            }
        }
    }

    public static class Combiner extends Reducer<TwoTexts, TwoIntegers, TwoTexts, TwoIntegers> {

        private TwoIntegers result = new TwoIntegers(0, 0);

        public void reduce(TwoTexts key, Iterable<TwoIntegers> values, Context context
        ) throws IOException, InterruptedException {
            long sumCar = 0;
            long sumPrice = 0;
            for (TwoIntegers val : values) {
                sumCar += val.getFirstValue();
                sumPrice += val.getSecondValue();
            }

            result.set(sumCar, sumPrice);
            context.write(key, result);
        }

    }

    public static void main(String[] args) throws Exception {

        //line which deletes output directory
        //FileUtils.deleteDirectory(new File(args[1]));

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "cars trucks");
        job.setJarByClass(CarsTrucks.class);

        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(Combiner.class);
        job.setReducerClass(IntSumReducer.class);

        job.setOutputKeyClass(TwoTexts.class);
        job.setOutputValueClass(TwoIntegers.class);
//        job.setNumReduceTasks(3);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}