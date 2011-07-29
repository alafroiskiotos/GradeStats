package business;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import charts.BarChart;
import charts.PieChart;

public class Main {

	public static void main(String[] args) {
		float pass = 0f;
		float passSum = 0f;
		int fail = 0;
		float mean = 0f;
		ScrapeGrades sg = new ScrapeGrades(args[0]);
		LinkedList<Integer> grades = sg.scrape();
		HashMap<Integer, Integer> exp = new HashMap<Integer, Integer>();
		for(int i = 0; i <= 10; i++)
			exp.put(i, 0);
		Iterator<Integer> gradesIt = grades.iterator();
		while(gradesIt.hasNext()){
			int tmpGrade = gradesIt.next();
			if(tmpGrade >= 5){
				passSum = passSum + tmpGrade;
				pass++;
			}else
				fail++;
			int curCount = exp.get(tmpGrade);
			exp.put(tmpGrade, ++curCount);
		}
		mean = passSum / pass;
		System.out.println("Pass: "+pass);
		System.out.println("Fail: "+fail);
		DecimalFormat df = new DecimalFormat("#.#");
		System.out.println("Mean: " + df.format(mean));
		
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new DrawPie(pass, fail));
		exec.execute(new DrawBar(exp));
	}

}
