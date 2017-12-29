Pod::Spec.new do |s|
  s.name           = "AdjustPurchase"
  s.version        = "1.0.0"
  s.summary        = "This is the iOS purchase SDK of adjust. You can read more about it at http://adjust.com."
  s.homepage       = "http://adjust.com"
  s.license        = { :type => 'MIT', :file => 'MIT-LICENSE' }
  s.author         = { "Ugljesa Erceg" => "ugi@adjust.com" }
  s.source         = { :git => "https://github.com/adjust/ios_purchase_sdk.git", :tag => "v1.0.0" }
  s.ios.deployment_target = '6.0'
  s.tvos.deployment_target = '9.0'
  s.framework      = 'SystemConfiguration'
  s.requires_arc   = true
  s.default_subspec = 'Core'

  s.subspec 'Core' do |co|
    co.source_files   = 'AdjustPurchase/*.{h,m}'
  end
end
