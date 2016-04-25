package org.al.statisticsutils;

/**
 * Created by lhscompsci on 4/25/16.
 */

// Below from http://www.cs.nyu.edu/courses/fall09/V22.0002-002/programs/programs17-0002/Deviation.html

/***********************************************************
 * Introduction to Computers and Programming (Fall 2009)   *
 * Professor Evan Korth                                    *
 *                                                         *
 * File Name: Deviation.java                               *
 * PIN: K0002F09084                                        *
 * Description: Calculating Standard Deviation             *
 *                                                         *
 * Focus:                                                  *
 * a. Calculating Standard Deviation                       *
 ***********************************************************/

// Beginning of class Deviation
public class Deviation {

    // Beginning of method main
//    public static void main(String[] args) {
//
//        // Declare and create an array for 10 numbers
//        double[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//
//        // Print numbers
//        printArray(numbers);
//
//        // Display mean and deviation
//        System.out.println("The mean is " + findMean(numbers));
//        System.out.println("The standard deviation is " +
//                findDeviation(numbers));
//
//    } // End of main

    /* Method for computing deviation of double values*/
    // Beginning of double findDeviation(double[])
    public static double findDeviation(double[] nums) {
        double mean = findMean(nums);
        double squareSum = 0;

        for (int i = 0; i < nums.length; i++) {
            squareSum += Math.pow(nums[i] - mean, 2);
        }

        return Math.sqrt((squareSum) / (nums.length - 1));
    } // End of double findDeviation(double[])

    /* Method for computing deviation of int values*/
    // Beginning of double findDeviation(int[])
    public static double findDeviation(int[] nums) {
        double mean = findMean(nums);
        double squareSum = 0;

        for (int i = 0; i < nums.length; i++) {
            squareSum += Math.pow(nums[i] - mean, 2);
        }

        return Math.sqrt((squareSum) / (nums.length - 1));
    } // End of double findDeviation(int[])

    /** Method for computing mean of an array of double values*/
    // Beginning of double findMean(double[])
    public static double findMean(double[] nums) {
        double sum = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }

        return sum / nums.length;
    } // End of double getMean(double[])

    /** Method for computing mean of an array of int values*/
    // Beginning of double findMean(int[])
    public static double findMean(int[] nums) {
        double sum = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }

        return sum / nums.length;
    } // End of double getMean(int[])

    /* Method for printing array */
    // Beginning of void printArray(double[])
    public static void printArray(double[] nums) {
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
        }

        System.out.println();
    } // End of void printArray(double[])

} // End of class Deviation

