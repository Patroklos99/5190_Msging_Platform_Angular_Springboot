import {Component, OnDestroy, OnInit} from "@angular/core";
import {Subscription} from "rxjs";
import {AuthenticationService} from "../../login/authentication.service";
import {Message} from "../message.model";
import {MessagesService} from "../messages.service";
import {FormBuilder, ReactiveFormsModule} from "@angular/forms";
import {AsyncPipe, DatePipe, NgForOf, NgIf} from "@angular/common";
import {LoginService} from '../../login/login.service';
import {MessagesComponent} from "../messages/messages.component";

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
		MessagesComponent
	],
	standalone: true
})
export class ChatPageComponent implements OnInit, OnDestroy {
	mensaje = this.messagesService.getMessages();
	username$ = this.authenticationService.getUsername();

	messageForm = this.fb.group({
		msg: "",
	});

	username: string | null = null;
	usernameSubscription: Subscription;

	messages: Message[] = [];

	constructor(
		private fb: FormBuilder,
		private messagesService: MessagesService,
		private authenticationService: AuthenticationService,
		private loginService: LoginService
	) {
		this.usernameSubscription = this.username$.subscribe((u) => {
			this.username = u;
		});
	}

	ngOnInit(): void {
	}

	ngOnDestroy(): void {
		if (this.usernameSubscription) {
			this.usernameSubscription.unsubscribe();
		}
	}

	onPublishMessage() {
		if (this.username && this.messageForm.valid && this.messageForm.value.msg) {
			this.messagesService.postMessage({
				text: this.messageForm.value.msg,
				username: this.username,
				timestamp: Date.now(),
			});
			this.mensaje.subscribe(m => this.messages = m);
		}
		this.messageForm.reset();
	}

	onLogout() {
		this.loginService.logout()
	}
}
