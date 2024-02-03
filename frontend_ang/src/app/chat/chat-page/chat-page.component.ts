import {Component, OnDestroy, OnInit} from "@angular/core";
import {Subscription} from "rxjs";
import {AuthenticationService} from "../../login/authentication.service";
import {ChatImageData, Message} from "../message.model";
import {MessagesService} from "../messages.service";
import {FormBuilder, ReactiveFormsModule} from "@angular/forms";
import {AsyncPipe, DatePipe, NgForOf, NgIf} from "@angular/common";
import {LoginService} from '../../login/login.service';
import {MessagesComponent} from "../messages/messages.component";
import {NewMsgFormComponent} from "../new-msg-form/new-msg-form.component";
import {Router} from '@angular/router';
import {WebsocketService} from "../../app.WebsocketService";
import {FileReaderService} from "../FileReaderService";
import {HttpErrorResponse} from "@angular/common/http";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
	selector: "app-chat-page",
	templateUrl: "./chat-page.component.html",
	styleUrls: ["./chat-page.component.css"],
	imports: [
		DatePipe,
		ReactiveFormsModule,
		AsyncPipe,
		NgForOf,
		NgIf,
		MessagesComponent,
		NewMsgFormComponent
	],
	standalone: true
})
export class ChatPageComponent implements OnInit, OnDestroy {
	mensaje$ = this.messagesService.getMessages();
	username$ = this.loginService.getUsername();
	messages: Message[] = [];
	username: string | null = null;
	usernameSubscription: Subscription;
	mensajeSubscripton: Subscription;

	constructor(
		private messagesService: MessagesService,
		private loginService: LoginService,
		private router: Router,
		private fileReader : FileReaderService,
	) {
		this.usernameSubscription = this.username$.subscribe((u) => {
			this.username = u;
		});
		this.mensajeSubscripton = this.mensaje$.subscribe(m => {
			this.messages = m;
		})
	}

	ngOnDestroy(): void {
		if (this.usernameSubscription) {
			this.usernameSubscription.unsubscribe()
		}
		if (this.mensajeSubscripton) {
			this.mensajeSubscripton.unsubscribe()
		}
	}

	ngOnInit(): void {
		this.messagesService.refreshMessages();
	}

	async onLogout() {
		await this.loginService.logout()
		await this.router.navigate(['/'])
		this.messagesService.clearMessages()
	}

	async onSendMsg(event: { msgTxt: string, file: File | null }) {
		if (this.username) {
			let imageData: ChatImageData | null = null;
			if (event.file) {
				let retour = await this.fileReader.readFile(event.file);
				imageData = {
					data: retour.data,
					type: retour.type
				}

			}
			try {
				await this.messagesService.postMessage({
					text: event.msgTxt,
					username: this.username,
					imageData: imageData
				});
			} catch (e) {
				if (e instanceof HttpErrorResponse) {
					console.log("error aca")
				}
			}
			// this.mensaje$.subscribe(m => this.messages = m);
		}
	}
}
