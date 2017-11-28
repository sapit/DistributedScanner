#!/bin/bash

display_help(){
    echo "Available commands are:
        -w|--worker
        -q|--queue
        -c|--client
        -s|--server
        Additional parameters:
        -p|--port 0000
        -h|--host X.X.X.X"
    exit 0
}

if [ $# -eq 0 ]; then
	display_help
fi

while [[ $# -gt 0 ]];
do
    opt="$1";
    shift;              #expose next argument
    case "$opt" in
        "-w"|"--worker" )
            target="target/worker.jar";;
        "-q"|"--queue" )
            target="target/messagequeue.jar";;
        "-c"|"--client" )
            target="target/client.jar";;
        "-s"|"--server" )
            target="target/server.jar";;

        "-p"|"--port" )
            port="$1"; shift;;
        "-h"|"--host" )
            host="$1"; shift;;
        *)
            echo >&2 "Invalid option: $opt"; display_help; exit 1;;
    esac
done

command="java -jar"
[ -z "$port" ] ||  command="$command -Dport=$port"      #if port is set, add as env var
[ -z "$host" ] ||  command="$command -Dhost=$host"      #if host is set, add as env var
eval "$command $target"
