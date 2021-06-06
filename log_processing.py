path_file = "/home/ubuntu/filename.txt"
# path_file = "logfile.txt"

# Using readlines()
file1 = open(path_file, 'r')
Lines = file1.readlines()

count = 0
for line in Lines:
    count += 1
    # TS, TJ
ts_tj = (line.strip()).split(",")
ave_ts = int(ts_tj[0]) / count
ave_tj = int(ts_tj[1]) / count


f=open("log_process_output.txt", "a+")
f.write("TS: " + str(ave_ts) + ", TJ :" + str(ave_tj)+ "\n")