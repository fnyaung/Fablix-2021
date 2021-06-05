path_file = "/home/ubuntu/logfile.txt"
# path_file = "logfile.txt"


# Using readlines()
file1 = open(path_file, 'r')
Lines = file1.readlines()

count = 0
ave_ts = 0
ave_tj = 0


for line in Lines:
    count += 1
    # TS, TJ
    ts_tj = (line.strip()).split(",")
    ave_ts += int(ts_tj[0])
    ave_tj += int(ts_tj[1])


ave_ts = ave_ts / count
ave_tj = ave_tj / count

f=open("log_process_output.txt", "a+")

f.write("TS: " + str(ave_ts) + ", TJ :" + str(ave_tj)+ "\n")