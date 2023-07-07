#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint selfie_liveness.podspec` to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'selfie_liveness'
  s.version          = '0.0.1'
  s.summary          = 'A new Flutter project.'
  s.description      = <<-DESC
A new Flutter project.
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*.{h,m,swift,xcdatamodeld}' 
  s.resource = 'Classes/*.storyboard'
  s.resource_bundles = {
    'Assets' => [
        'Assets/*.png'
]
   
  }
  s.dependency 'Flutter'
  s.platform = :ios, '10.0'
  s.ios.dependency 'GoogleMLKit/FaceDetection', '3.2.0'
  #s.ios.dependency 'GoogleMobileVision/MVDataOutput'
  s.ios.dependency 'GoogleMLKit/TextRecognition'
  s.static_framework = true

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'
end
