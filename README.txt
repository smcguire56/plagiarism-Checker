plagiarism Checker	G00330886	Sean McGuire
====================================================

This project is deigned to find the precentage of similarity in 2 files.
The user is asked to input the name of the files, the program will read them both on
seperate threads and will start a consumer thread for the jaccard calculation.

1) initially the program will ask for the file names
2) then ask for the k size which is the size of amount of min-hashes
3) then will ask for the shingle size value
4) and finally the thread pool size.


Example:
========================================
Starting new Document Similarity Checker
========================================

Please enter Document 1's Name: 
war
Please enter Document 2's Name: 
war2
Please enter k size: 
500
Please enter shingle size: 
3
Please enter thread pool size: 
300
Jaccard: 96.07843 %.
Checks: 108583876 times.


