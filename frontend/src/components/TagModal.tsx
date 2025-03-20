import { FormEvent, ChangeEvent, useState, useEffect } from 'react';
import { Modal, Button, Label, TextInput } from 'flowbite-react';
import { useAppDispatch, useAppSelector } from '../app/hooks';
import { createTag, selectTagToEdit, setTagToEdit, Tag, updateTag } from '../features/tags/tagSlice';
import { toast } from 'react-toastify';

interface TagModalProps {
    isOpen: boolean;
    onClose: () => void;
}

export function TagModal({ isOpen, onClose }: TagModalProps) {
    const dispatch = useAppDispatch();
    const tag = useAppSelector(selectTagToEdit);
    const [name, setName] = useState('');
    const [nameError, setNameError] = useState('');
    const [serverError, setServerError] = useState('');

    const handleNameChange = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setName(value);
        if (!value.trim()) {
            setNameError('Tag name is required.');
        } else {
            setNameError('');
        }
    };

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();

        if (!name.trim()) {
            setNameError('Tag name is required.');
            return;
        }

        try {
            if (tag) {
                await dispatch(updateTag({ id: tag.id, name })).unwrap();
                dispatch(setTagToEdit(null))
                toast.success('Tag updated successfully');
            } else {
                await dispatch(createTag({ name })).unwrap();
                toast.success('Tag created successfully');
            }

            setName('');
            setNameError('');
            setServerError('');
            onClose();
        } catch (err: any) {
            console.log(err);

            if (err && err.details && Array.isArray(err.details)) {
                err.details.forEach((detail: { field: string; error: string }) => {
                    if (detail.field === "name") {
                        setNameError(detail.error);
                    }
                });
            } else {
                setServerError(typeof err === "string" ? err : err.message || "Registration failed");
            }
        };
    }

    useEffect(() => {
        setName(tag?.name || '');
    }, [tag]);

    return (
        <Modal show={isOpen} onClose={onClose}>
            <Modal.Header>Create New Tag</Modal.Header>
            <Modal.Body>
                <form onSubmit={handleSubmit} className="space-y-6">
                    {serverError && <p className="text-red-600 text-sm">{serverError}</p>}
                    <div>
                        <Label htmlFor="tagName" value="Tag Name" />
                        <TextInput
                            id="tagName"
                            placeholder="Example: Platformer"
                            required
                            value={name}
                            onChange={handleNameChange}
                            color={nameError ? 'failure' : ''}
                            helperText={nameError && <span className="text-red-600">{nameError}</span>}
                        />
                    </div>
                </form>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={handleSubmit}>Create Tag</Button>
                <Button color="gray" onClick={onClose}>
                    Cancel
                </Button>
            </Modal.Footer>
        </Modal>
    );
}
