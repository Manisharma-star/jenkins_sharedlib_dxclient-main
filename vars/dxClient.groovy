
def printConfig(Map config = [:]) {
    echo 'Configuration:'
    echo "   dxWPSCredentials: ${config.dxWPSCredentials}"
    echo "   dxWASCredentials: ${config.dxWASCredentials}"
    echo "   dxProtocol: ${config.dxProtocol}"
    echo "   dxConnectProtocol: ${config.dxConnectProtocol}"
    echo "   hostname: ${config.hostname}"
    echo "   dxPort: ${config.dxPort}"
    echo "   dxSoapPort: ${config.dxSoapPort}"
    echo "   dxConnectPort: ${config.dxConnectPort}"
    echo "   dxContextRoot: ${config.dxContextRoot}"
    echo "   contenthandlerPath: ${config.contenthandlerPath}"
     echo "   dxProfileName: ${config.dxProfileName}"
    
}

def call() {
    echo "dxclient is called::::::::"
    configFileProvider([configFile(fileId: 'dx-targets.yaml', variable: 'DXCLIENT_SETTINGS')]) {
         echo "configFileProvider is called::::::::"
        Map config = [:]
        def fileConfig = readYaml(file: "$DXCLIENT_SETTINGS")
         echo "@@@@@@@@@@@@@@@@@@@@@@@"
         echo "fileConfig : ${fileConfig}"
        fileConfig.environments.each { envName, envConfig ->
            // Print the branch value
       echo "envConfig.branch : ${envConfig.branch}"
            echo " env.GIT_BRANCH : ${ env.GIT_BRANCH}"
            echo "Environment: ${envName}, Branch: ${envConfig.branch} GIT_BRANCH: ${env.GIT_BRANCH}"

            if ('origin/' + envConfig.branch == "origin/main") {
                echo "EnvConfig: ${envConfig}"
                echo "Loading configuration for ${envName}"
                config['dxWPSCredentials'] = envConfig.dxWPSCredentials
                config['dxWASCredentials'] = envConfig.dxWASCredentials
                config['dxProtocol'] = envConfig.dxProtocol
                config['dxConnectProtocol'] = envConfig.dxConnectProtocol
                config['hostname'] = envConfig.hostname
                config['dxPort'] = envConfig.dxPort
                config['dxSoapPort'] = envConfig.dxSoapPort
                config['dxConnectPort'] = envConfig.dxConnectPort
                config['dxContextRoot'] = envConfig.dxContextRoot
                config['contenthandlerPath'] = envConfig.contenthandlerPath
                config['dxProfileName'] = envConfig.DX_PROFILENAME
                printConfig(config)
                
               

          

              script {  
                  Exception caughtException = null;
              catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                try {
                  command = "deploy-application -hostname ${config.hostname} -dxProtocol ${config.dxProtocol} -dxPort ${config.dxPort} -dxUsername wpsadmin -dxPassword Avn3tNPR -dxConnectPort ${config.dxPort} -dxConnectUsername wpsadmin -dxConnectPassword Avn3tNPR -applicationFile C:/ProgramData/Jenkins/.jenkins/workspace/Test/target/globalUserProfile.ear-0.0.1-SNAPSHOT.ear -applicationName globalUserProfile -dxProfileName ${config.dxProfileName}"

              // TODO : check for generic artifact path   
                  bat "set NODE_TLS_REJECT_UNAUTHORIZED=0"
                  bat "node C:/Users/C23597/Software/hcl-dxclient-v95_CF220_20240522-1932/dxclient/bin/dxclient ${command}"

                } catch (Throwable e) {
                  caughtException = e;
                }
                if (caughtException) {
                  error caughtException.message
                }
              }  
                  
              }
                  // Use the credentials as needed
                    // For example, you can add them to the config map
                    config['dxClientUser'] = env.DXCLIENT_USER
                    config['dxClientPass'] = env.DXCLIENT_PASS
                
                
                return config
            }
        }
    }
}
