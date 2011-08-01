/*
   Copyright (C) 2011
   Kouzoupis Antonis
   
   This file is part of GradeStats.
 
    GradeStats is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as 
    published by the Free Software Foundation, either version 3 of the 
    License, or (at your option) any later version.

    GradeStats is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with GradeStats.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		if(args.length == 0){
			StringBuilder sb = new StringBuilder();
			sb.append("No url specified").append("\n");
			sb.append("usage: java -jar GradeStats.jar URL").append("\n");
			sb.append("URL - The url of the results page of the lesson you ");
			sb.append("are interested in.").append("\n");
			
			System.out.println(sb);
			System.exit(0);
		}
		float pass = 0f;
		float passSum = 0f;
		int fail = 0;
		float mean = 0f;
		ScrapeGrades sg = new ScrapeGrades(args[0]);
		ScrapeResult sr = sg.scrape();
		LinkedList<Integer> grades = sr.getGrades();
		String lesson = sr.getLesson();
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
		exec.execute(new DrawPie(pass, fail, lesson));
		exec.execute(new DrawBar(exp, lesson));
	}

}
