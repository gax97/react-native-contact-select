#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ContactSelect, NSObject)

RCT_EXTERN_METHOD(
                  selectContact: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

@end

