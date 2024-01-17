import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-new-msg-form',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './new-msg-form.component.html',
  styleUrl: './new-msg-form.component.css'
})
export class NewMsgFormComponent {
  @Output() sendMsg = new EventEmitter<string>();

  messageForm = this.fb.group({
    msg: "",
  });

  constructor(
    private fb: FormBuilder) {
  }

  onPublishMessage() {
    if (this.messageForm.valid) {
      this.sendMsg.emit(this.messageForm.value.msg!)
      this.messageForm.reset();
    }
  }


}
