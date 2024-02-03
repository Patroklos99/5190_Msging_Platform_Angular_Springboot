import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder, ReactiveFormsModule} from "@angular/forms";
import {FileReaderService} from "../FileReaderService";

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
	@Output() sendMsg = new EventEmitter<{msgTxt:string, file:File|null}>();

	messageForm = this.fb.group({
		msg: "",
	});

	file: File | null = null;

	constructor(private fb: FormBuilder,
				private fileReader: FileReaderService) {
	}

	onPublishMessage() {
		if (this.messageForm.valid && this.messageForm.value.msg) {
			if (this.file)
				this.sendMsg.emit({msgTxt: this.messageForm.value.msg, file: this.file})
			else
				this.sendMsg.emit({msgTxt: this.messageForm.value.msg, file: null})
			this.messageForm.reset();
			this.file = null;
		}
	}

	fileChanged(event : any) {
		this.file = event.target.files[0];
	}


}
