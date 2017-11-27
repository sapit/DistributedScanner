#!/bin/bash

display_help(){
    echo "Available commands are:
        --w|--worker\n
        --q|--queue\n
        --c|--client\n
        --s|--server\n
        Additional parameters:\n
        --p|--port\n
        --h|--host\n"
}

if [ "$1" == "-h" ] || [ $# -eq 0 ]; then
    display_help
    exit 0
fi

while [[ $# -gt 0 ]] && [[ ."$1" = .--* ]] ;
do
    opt="$1";
    shift;              #expose next argument
    case "$opt" in
        "--w"|"--worker" )
            target="target/worker.jar";;
        "--q"|"--queue" )
            target="target/messagequeue.jar";;
        "--c"|"--client" )
            target="target/client.jar";;
        "--s"|"--server" )
            target="target/server.jar";;

        "--p"|"--port" )
            port="$1"; shift;;
        "--h"|"--host" )
            host="$1"; shift;&
        *)
            echo >&2 "Invalid option: $@"; display_help; exit 1;;
    esac
    echo 'hui'
done

command="java -jar"
[ -z "$port" ] ||  command="$command -Dport=$port"      #if port is set, add as env var
[ -z "$host" ] ||  command="$command -Dhost=$host"      #if host is set, add as env var

eval "$command $target"