
import Flutter
import UIKit

public class SwiftSelfieLivenessPlugin: NSObject, FlutterPlugin, DismissProtocol {
    var receivedPath = String()
    var resultCallback : FlutterResult!
    static var mregistrar:FlutterPluginRegistrar!
    
    func sendData(filePath: String) {
            receivedPath = filePath
            if resultCallback != nil{
                resultCallback(filePath)
            }else{
                resultCallback("")
            }
        }
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "elatech_liveliness_plugin", binaryMessenger: registrar.messenger())
    let instance = SwiftSelfieLivenessPlugin()
      mregistrar = registrar;
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
      resultCallback=result;
      if(call.method=="detectliveliness"){
          detectFace(call:call)
      }
  }
    
    func detectFace(call:FlutterMethodCall){
                   var msgselfieCapture = ""
                   var msgBlinkEye = ""
                    var massetPath="";
                    var mpoweredBy=""
                   guard let args = call.arguments else {
                       return
                   }
                   if let myArgs = args as? [String: Any],
                       let assetPath = myArgs["assetPath"] as? String,
                       
                       let poweredBy = myArgs["poweredBy"] as? String,
                       let captureText = myArgs["msgselfieCapture"] as? String,
                       let blinkText = myArgs["msgBlinkEye"] as? String{
                       massetPath = assetPath
                       mpoweredBy = poweredBy
                       msgselfieCapture = captureText
                       msgBlinkEye = blinkText
                   }
              self.detectLiveness(captureMessage: msgselfieCapture, blinkMessage: msgBlinkEye, assetPath: massetPath, poweredBy: mpoweredBy)
              
            }
       
       
    public func detectLiveness(captureMessage: String, blinkMessage: String, assetPath: String, poweredBy: String){
           if let viewController = UIApplication.shared.windows.first?.rootViewController as? FlutterViewController{
               let storyboardName = "MainLive"
               let storyboardBundle = Bundle.init(for: type(of: self))
               let storyboard = UIStoryboard(name: storyboardName, bundle: storyboardBundle)
               if let vc = storyboard.instantiateViewController(withIdentifier: "TestViewController") as? TestViewController {
                   let key = SwiftSelfieLivenessPlugin.mregistrar?.lookupKey(forAsset: assetPath)
                   vc.captureMessageText = captureMessage
                   vc.modalPresentationStyle = .fullScreen
                   vc.blinkMessageText = blinkMessage
                   vc.assetPath = key
                   viewController.present(vc, animated: true, completion: nil)
                   vc.dismissDelegate = self
                   //vc.poweredBy = poweredBy
               }
           }
       }
}
