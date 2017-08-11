// File Checker
// Gregor Duerr, Trivadis AG

import java.nio.charset.Charset

v_path     = '/c:/Temp/FF/PRD/MOFIS/20170615/'
v_read_filename = 'mofis_codetexte_20170615.txt'
v_summary_filename = 'summary.txt'
v_field_separator = "|"
v_compare_with_first_line=0
v_compare_with_previous_line=1
v_skip_first_line=1


def sum_file = new File('/c:/Temp/FF/PRD/MOFIS/20170615/summary.txt')
def read_file = new File('c:/Temp/FF/PRD/MOFIS/20170615/mofis_codetexte_20170615.txt')
def lines = read_file.readLines()
def line_size_1
def delimiter_count_1

line_count = lines.size()
total_delimiter   = read_file.getText().count(v_field_separator)
delimiter_count_1 = lines[0].count(v_field_separator)
if (v_skip_first_line==0) { delimiter_count_1 = lines[0].count(v_field_separator) } else { delimiter_count_1 = lines[1].count(v_field_separator) }
if (v_skip_first_line==0) { line_size_1 = lines[0].size() } else { line_size_1 = lines[1].size() }
total_tabulator = read_file.getText().count("\t")
mittelwert = total_delimiter / line_count
is_ascii = Charset.forName("US-ASCII").newEncoder().canEncode( read_file.getText() )
is_utf8 = Charset.forName("UTF-8").newEncoder().canEncode( read_file.getText() )

//int code = (int)"A"
//def characters = str.chars  
//0 <= code && code <= 127
//if (code >=0 && code <= 127) { println "code="+code }

sum_file.write  "SUMMARY:"
sum_file.append "\n"
sum_file.append "========"
sum_file.append "\n\n"
sum_file.append "Execution date and time:    " + new Date().format('dd.MM.yyyy HH:mm:ss')
sum_file.append "\n"
sum_file.append "Input filename:             " + v_path + v_read_filename
sum_file.append "\n"
sum_file.append "Output filename:            " + v_path + v_summary_filename
sum_file.append "\n"
sum_file.append "Is US-ASCII?:               " + is_ascii.toString().toUpperCase()
sum_file.append "\n"
sum_file.append "Is UTF-8?:                  " + is_utf8.toString().toUpperCase()
sum_file.append "\n"
sum_file.append "Skip first line?:           "
if (v_skip_first_line==1) {sum_file.append "TRUE"} else {sum_file.append "FALSE"}
sum_file.append "\n"
sum_file.append "Field separator:            " + v_field_separator
sum_file.append "\n"
sum_file.append "Total of lines:             " + line_count.toString()
sum_file.append "\n"
//sum_file.append "Total of white spaces:    " + total_whitespace.toString()
sum_file.append "\n"
sum_file.append "Total of tabulators:        " + total_tabulator.toString()
sum_file.append "\n"
sum_file.append "Total of field separators   " + "('"+v_field_separator + "'): " + total_delimiter.toString()
sum_file.append "\n"
sum_file.append "Average:                    " + mittelwert.toString() + " field separators per line"
sum_file.append "\n"
sum_file.append "Compare with first line:    "
if (v_compare_with_first_line==1) {sum_file.append "TRUE"} else {sum_file.append "FALSE"}
sum_file.append "\n"
sum_file.append "Compare with previous line: "
if (v_compare_with_previous_line==1) {sum_file.append "TRUE"} else {sum_file.append "FALSE"}
sum_file.append "\n\n"
sum_file.append "Description of warnings:"
sum_file.append "\n"
sum_file.append "========================"
sum_file.append "\n\n"
sum_file.append "FCW-0001: Number of field separators different from first line (" + delimiter_count_1 + ")"
sum_file.append "\n"
sum_file.append "FCW-0002: Number of field separators different from previous line"
sum_file.append "\n"
sum_file.append "FCW-0003: Line size different from first line (" + line_size_1 + ")"
sum_file.append "\n"
sum_file.append "FCW-0004: Line size different from previous line"
sum_file.append "\n\n"
sum_file.append "Detected warnings:"
sum_file.append "\n"
sum_file.append "==================="
sum_file.append "\n\n"

delimiter_cnt_old=delimiter_count_1
line_size_old=line_size_1
warning = 0
i = 1
read_file.withReader { reader -> 
    while (line = reader.readLine()) {
        if (v_skip_first_line==1 && i==1) {} 
        else {
        delimiter_cnt=line.count(v_field_separator)
        if (delimiter_cnt != delimiter_count_1 && v_compare_with_first_line==1) {
          warning=1
          sum_file.append "FCW-0001: " + "Line number " + i.toString() + ": " + "number of field seperators=" + delimiter_cnt.toString()+"\n"
        }
        if (delimiter_cnt != delimiter_cnt_old && v_compare_with_previous_line==1) {
          warning=1
          sum_file.append "FCW-0002: " + "Line number " + i.toString() + ": " + "number of field seperators=" + delimiter_cnt.toString()+"\n"
        }
        delimiter_cnt_old = delimiter_cnt
        
        //def characters = line.chars
        //if (characters[0].isWhitespace()) { println "ws"} else { println "no" }
        
        line_size = line.size()
        if (line_size != line_size_1 && v_compare_with_first_line==1) {
		  warning=1
          sum_file.append "FCW-0003: " + "Line number " + i.toString() + ": " + "linesize=" + line_size.toString()+"\n"
        }
        if (line_size != line_size_old && v_compare_with_previous_line==1) {
		  warning=1
          sum_file.append "FCW-0004: " + "Line number " + i.toString() + ": " + "linesize=" + line_size.toString()+"\n"
        }
		line_size_old = line_size
    }
        i++
    }
    if (warning == 0) {
      sum_file.append "no warnings"
    }
}
