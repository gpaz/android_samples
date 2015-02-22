package com.example.demoservice.aidl;


interface IPrimeNumberGenerator
{
    long[] generatePrimeNumbers(in long from, in long to, in int count);
}