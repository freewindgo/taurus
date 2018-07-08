#!/bin/bash
function prepare_conf_file {
   local conf_file=$1
   local new_conf_file=$2
   local servlet_path="servlet-path"
   local server_servlet_path="server.servlet-path"
   local file=$1
   sed -e "s/${server_servlet_path}/server_servlet_path/g" $conf_file|sed -e "s/${servlet_path}/servlet_path/g"|sed -e 's/\${.*\..*}//g' >${new_conf_file}
}

function parse_yaml1 {
   local file=$1
   local prefix=$2
   local s='[[:space:]]*' w="[a-zA-Z0-9_\-]*" fs=$(echo @|tr @ '\034')
   special_char_replace $file
   sed -ne "s|^\($s\):|\1|" \
        -e "s|^\($s\)\($w\)$s:$s[\"']\(.*\)[\"']$s\$|\1$fs\2$fs\3|p" \
        -e "s|^\($s\)\($w\)$s:$s\(.*\)$s\$|\1$fs\2$fs\3|p" ${file}.new |
   awk -F$fs '{
      indent = length($1)/2;
      vname[indent] = $2;
      for (i in vname) {if (i > indent) {delete vname[i]}}
      if (length($3) > 0) {
         vn=""; for (i=0; i<indent; i++) {vn=(vn)(vname[i])("_")}
         printf("%s%s%s=\"%s\"\n", "'$prefix'",$vn, $2, $3);
      }
   }'
}


function parse_yaml() {
    local yaml_file="${1}"
    local prefix=$2
    local s
    local w
    local fs

    s='[[:space:]]*'
    w='[a-zA-Z0-9_]*'
    fs="$(echo @|tr @ '\034')"

    (
        sed -ne 's/--//g; s/\"/\\\"/g; s/\#.*//g; s/\s*$//g;' \
            -e  "s|^\($s\)\($w\)$s:$s\"\(.*\)\"$s\$|\1$fs\2$fs\3|p" \
            -e "s|^\($s\)\($w\)$s[:-]$s\(.*\)$s\$|\1$fs\2$fs\3|p" |
        awk -F"$fs" '{
            indent = length($1)/2;
            if (length($2) == 0) { conj[indent]="+";} else {conj[indent]="";}
            vname[indent] = $2;
            for (i in vname) {if (i > indent) {delete vname[i]}}
                if (length($3) > 0) {
                    vn=""; for (i=0; i<indent; i++) {vn=(vn)(vname[i])("_")}
                    printf("%s%s%s%s=(\"%s\")\n", "'"$prefix"'",vn, $2, conj[indent-1],$3);
                }
            }' |
        sed 's/_=/+=/g'
    ) < "$yaml_file"
}

function get_shutdown_url {
  local conf_file=$1
  local new_conf_file="${conf_file}.new"
  prepare_conf_file "$conf_file" "$new_conf_file"
  eval $(parse_yaml $new_conf_file)
  local port="$server_port"
  local servlet_path="$server_servlet_path"
  if [ "${servlet_path}xx" == "xx" -o "${servlet_path}" == "." -o "${servlet_path}" == "./" ];then
     servlet_path="/"
  fi
  echo "$servlet_path"|grep -E "^/" 1>/dev/null 2>&1
  if [ $? != 0 ];then
     servlet_path="/${servlet_path}"
  fi
  echo "$servlet_path"|grep -E "/$" 1>/dev/null 2>&1
  if [ $? != 0 ];then
     servlet_path="${servlet_path}/"
  fi

  local url="http://127.0.0.1:${port}${servlet_path}shutdown"
  rm $new_conf_file 1>/dev/null 2>&1
  echo $url
}

