
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

            if ('origin/' + envConfig.branch == "main") {
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

                printConfig(config)
                
                withCredentials([usernamePassword(credentialsId: 'dxclient-cred', usernameVariable: 'DXCLIENT_USER', passwordVariable: 'DXCLIENT_PASS')]) {
                    sh 'echo DXCLIENT_USER: $DXCLIENT_USER'
                    sh 'echo DXCLIENT_PASS: $DXCLIENT_PASS'
                    
                    // Use the credentials as needed
                    // For example, you can add them to the config map
                    config['dxClientUser'] = env.DXCLIENT_USER
                    config['dxClientPass'] = env.DXCLIENT_PASS
                }
                
                return config
            }
        }
    }
}
