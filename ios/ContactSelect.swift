import ContactsUI
import Contacts


@objc(ContactSelect)
class ContactSelect: NSObject, CNContactPickerDelegate, UIActionSheetDelegate {

    var resolve: RCTPromiseResolveBlock?
    var reject:RCTPromiseRejectBlock?

    @objc(selectContact:rejecter:)
    func selectContact(resolve:@escaping RCTPromiseResolveBlock,reject:@escaping RCTPromiseRejectBlock) -> Void {
        self.resolve = resolve;
        self.reject = reject;
        openContactPicker()
    }


    @objc
    func openContactPicker() {
        DispatchQueue.main.async {
            let contactPicker = CNContactPickerViewController()
            contactPicker.delegate = self
            contactPicker.displayedPropertyKeys = [CNContactPhoneNumbersKey]
            contactPicker.predicateForEnablingContact = NSPredicate(format: "phoneNumbers.@count > 0")
            contactPicker.predicateForSelectionOfProperty = NSPredicate(format: "key == 'phoneNumbers'")
            contactPicker.modalPresentationStyle = .formSheet
            let root = UIApplication.shared.delegate?.window??.rootViewController
            root?.present(contactPicker, animated: true)
        }
    }

    func contactPickerDidCancel(_ picker: CNContactPickerViewController) {
        reject?(ContactsError.Canceled.rawValue, "User Cancelled", nil)
    }

    func contactPicker(_ picker: CNContactPickerViewController, didSelect contact: CNContact) {
        var data = [String: Any]()

        addString(data: &data, key: "name", value: contact.givenName + " " + contact.familyName )
        addString(data: &data, key: "phone", value: contact.phoneNumbers.first?.value.stringValue)
        addString(data: &data, key: "id", value: contact.identifier)

        resolve!(data)
    }

    func addString(data: inout [String: Any], key: String, value: String?) {
        if let x = value, !x.isEmpty {
            data[key] = x
        }
    }

}

enum ContactsError: String {
    case Failed, Canceled, Unknown
}
